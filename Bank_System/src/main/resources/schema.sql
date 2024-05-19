CREATE TABLE bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    transaction_flat_fee_amount DECIMAL(10, 2) NOT NULL,
    transaction_percent_fee_value DECIMAL(5, 2) NOT NULL,
    total_transaction_fee_amount DECIMAL(15, 2) DEFAULT 0,
    total_transfer_amount DECIMAL(15, 2) DEFAULT 0
);

CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    bank_id BIGINT,
    FOREIGN KEY (bank_id) REFERENCES bank(id)
);

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(15, 2) NOT NULL,
    reason VARCHAR(255),
    originating_account_id BIGINT,
    resulting_account_id BIGINT,
    FOREIGN KEY (originating_account_id) REFERENCES account(id),
    FOREIGN KEY (resulting_account_id) REFERENCES account(id)
);
