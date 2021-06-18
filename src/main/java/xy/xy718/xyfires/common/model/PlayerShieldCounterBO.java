package xy.xy718.xyfires.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerShieldCounterBO {
    private Integer shieldValue;
    private Integer shieldCounter;
    public static PlayerShieldCounterBO init(){
        return new PlayerShieldCounterBO(5,20);
    }
    public void shieldTickIgnore(){
        this.shieldCounter--;
    }
}
