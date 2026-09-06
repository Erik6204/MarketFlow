package com.example.marketflow.checkout;

import java.math.BigDecimal;

public record CheckoutSummaryDto(BigDecimal total) {//нужен для передачи итоговой стоимости выбранных
//  товаров перед созданием заказа.
}
