package com.cornelmarck.sunnycloud.repository;

import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.SitePrimaryKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SiteRepository extends CrudRepository<Site, SitePrimaryKey> {
    List<Site> findAllByUserIdAndSortKeyStartsWith(String s, String siteDetails);
}
