package com.cornelmarck.sunnycloud.repository;

import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.SitePrimaryKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface SiteRepository extends CrudRepository<Site, SitePrimaryKey> {
    List<Site> findSitesByUserIdAndSortKeyStartsWith(String userId, String sortKeyFilter);
}
