package com.bitmain.intelligent.tax.database;

import com.bitmain.intelligent.tax.database.entity.TaxData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface TaxMapper {




    @Insert("INSERT INTO taxdata(userID,grossPay,fee,tax,realSalary,resultDesc) VALUES(#{userID},#{grossPay},#{fee},#{tax},#{realSalary},#{resultDesc})")
    @Options(useGeneratedKeys = true)
    void addTaxData(TaxData taxData);

    @Select("SELECT * from taxdata where userID=#{arg0}")
    TaxData findTaxDataIfExist(long userID);

    @Update("UPDATE taxdata SET  grossPay=#{grossPay},fee=#{fee},tax=#{tax},realSalary=#{realSalary},resultDesc=#{resultDesc} where userID=#{userID}")
    void updateTaxData(TaxData taxData);
}
