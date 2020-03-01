package kmeshkov.revolut.model.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account{
    private Long id;
    private boolean isActive = true;
    private BigDecimal balance = BigDecimal.ZERO;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date accountCreated = Calendar.getInstance().getTime();
    private Long ownerUid;
    //TODO add account type
    public void disable(){
        isActive = false;
    }
}
