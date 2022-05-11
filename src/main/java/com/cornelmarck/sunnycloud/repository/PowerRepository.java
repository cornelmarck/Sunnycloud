package com.cornelmarck.sunnycloud.repository;

import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.PowerPrimaryKey;
import org.springframework.data.repository.CrudRepository;

public interface PowerRepository extends CrudRepository<Power, PowerPrimaryKey> {
}
