package com.flora.FlorDTO;

import java.math.BigDecimal;

    public class ClientResponseDTO {

        private long id;
        private String name;
        private String phoneNumber;
        private BigDecimal balance;

        public ClientResponseDTO(
                long id,
                String name,
                String phoneNumber,
                BigDecimal balance) {

            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.balance = balance;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public BigDecimal getBalance() {
            return balance;
        }
    }
