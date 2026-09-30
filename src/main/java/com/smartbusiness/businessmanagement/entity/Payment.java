package com.smartbusiness.businessmanagement.entity;

import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import com.smartbusiness.businessmanagement.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime paidAt = LocalDateTime.now();

    @Column(precision = 15, scale = 2)
    private BigDecimal amountTendered;

    @Column(precision = 15, scale = 2)
    private BigDecimal changeAmount;
}