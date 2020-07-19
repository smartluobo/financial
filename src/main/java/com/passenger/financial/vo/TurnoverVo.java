package com.passenger.financial.vo;

import com.passenger.financial.entity.Driver;
import com.passenger.financial.entity.TurnoverRecord;
import lombok.Data;

@Data
public class TurnoverVo {

    private Driver driver;

    private TurnoverRecord turnoverRecord;
}
