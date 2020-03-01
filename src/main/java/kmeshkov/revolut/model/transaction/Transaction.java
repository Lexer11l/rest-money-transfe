package kmeshkov.revolut.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
public class Transaction{
    private Long id;
    private long toAccount;
    private long fromAccount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date transactionDate = Calendar.getInstance().getTime();
    private BigDecimal amount;
    private TransactionType transactionType;
}
