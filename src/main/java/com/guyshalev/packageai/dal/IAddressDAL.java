package com.guyshalev.packageai.dal;

import com.guyshalev.packageai.model.OrderPersonAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressDAL extends JpaRepository<OrderPersonAddress, Long>{


}
