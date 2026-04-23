package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue
    private long id;
    
    private long senderId;
    private long recipientId;
    private float amount;
    private float incentive;

    protected TransactionRecord() {
    }

    public TransactionRecord(long senderId, long recipientId, float amount, float incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }

    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getIncentive() {
        return incentive;
    }

    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }
}
