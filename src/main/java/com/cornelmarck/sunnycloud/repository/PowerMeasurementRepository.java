package com.cornelmarck.sunnycloud.repository;

import com.cornelmarck.sunnycloud.model.PowerMeasurement;
import com.cornelmarck.sunnycloud.model.PowerMeasurementPrimaryKey;
import org.springframework.data.repository.CrudRepository;

public interface PowerMeasurementRepository extends CrudRepository<PowerMeasurement, PowerMeasurementPrimaryKey> {
}
