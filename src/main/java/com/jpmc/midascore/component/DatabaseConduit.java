package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseConduit {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConduit.class);
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public boolean processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            logger.warn("Transaction rejected: sender or recipient not found");
            return false;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Transaction rejected: insufficient funds for user " + sender.getName());
            return false;
        }

        if (transaction.getAmount() <= 0) {
            logger.warn("Transaction rejected: invalid amount");
            return false;
        }

        // Call the incentive API
        float incentiveAmount = 0;
        try {
            Incentive incentive = restTemplate.postForObject(INCENTIVE_API_URL, transaction, Incentive.class);
            if (incentive != null) {
                incentiveAmount = incentive.getAmount();
            }
        } catch (Exception e) {
            logger.warn("Incentive API call failed: " + e.getMessage());
        }

        // Update balances: deduct from sender, add transaction amount + incentive to recipient
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);
        
        transactionRecordRepository.save(new com.jpmc.midascore.entity.TransactionRecord(
                transaction.getSenderId(), 
                transaction.getRecipientId(), 
                transaction.getAmount(), 
                incentiveAmount));

        logger.info("Transaction processed: " + sender.getName() + " -> " + recipient.getName()
                + " amount=" + transaction.getAmount() + " incentive=" + incentiveAmount);
        
        UserRecord wilbur = userRepository.findById(9L);
        if (wilbur != null) {
            logger.info("WILBUR_BALANCE=" + wilbur.getBalance());
        }
        
        return true;
    }
}
