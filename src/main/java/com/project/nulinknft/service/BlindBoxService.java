package com.project.nulinknft.service;

import com.project.nulinknft.dto.ReferDTO;
import com.project.nulinknft.entity.BlindBox;
import com.project.nulinknft.repository.BlindBoxRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlindBoxService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BlindBoxRepository blindBoxRepository;

    public BlindBoxService(BlindBoxRepository blindBoxRepository) {
        this.blindBoxRepository = blindBoxRepository;
    }

    public Page<BlindBox> list(String startTime, String endTIme, String user, String referralAddress, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("createTime")));
        Specification<BlindBox> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTIme)) {
                predicates.add(criteriaBuilder.between(root.get("time"), startTime, endTIme));
            }
            if (StringUtils.isNotEmpty(referralAddress)) {
                predicates.add(criteriaBuilder.equal(root.get("recommender"), referralAddress));
            }
            if (StringUtils.isNotEmpty(user)) {
                predicates.add(criteriaBuilder.equal(root.get("user"), user));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return blindBoxRepository.findAll(specification, pageable);
    }

    public Page<ReferDTO> findReferDTOPage(String time, String to,  String referralLevel, String referralAddress, String recommendedAddress, Integer page, Integer size){
        StringBuilder listSqlBuilder = new StringBuilder("select * from (\n" +
                "select b.time as time, '1' as level, b.user as referredUser  \n" +
                "from blind_box b where recommender = :referralAddress \n" +
                "UNION\n" +
                "select x.time as time, '2' as level,  x.user as referredUser \n" +
                "from blind_box x \n" +
                "where recommender in (select user from blind_box where recommender = :referralAddress)) t \n" +
                "where 1= 1 ");

        if (StringUtils.isNotEmpty(time) && StringUtils.isNotEmpty(to)){
            listSqlBuilder.append(" and from_unixtime(t.time) between from_unixtime(:startTime) and FROM_UNIXTIME(:endTime)");
        }
        if (StringUtils.isNotEmpty(referralLevel)){
            listSqlBuilder.append(" and level = :referralLevel ");
        }
        if (StringUtils.isNotEmpty(recommendedAddress)){
            listSqlBuilder.append(" and referredUser = :recommendedAddress ");
        }
        listSqlBuilder.append(" order by from_unixtime(t.time) desc limit :offset, :rowCount ");
        Query listQuery = entityManager.createNativeQuery(listSqlBuilder.toString());
        listQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(ReferDTO.class));
        if (null != time && null != to){
            listQuery.setParameter("startTime", time);
            listQuery.setParameter("to", to);
        }
        if (null != referralLevel){
            listQuery.setParameter("referralLevel", referralLevel);
        }
        if (null != recommendedAddress){
            listQuery.setParameter("recommendedAddress", recommendedAddress);
        }
        listQuery.setParameter("referralAddress", referralAddress);
        listQuery.setParameter("offset", (page - 1) * size);
        listQuery.setParameter("rowCount", size);

        List referDTOS = listQuery.getResultList();
        long count = findReferralCount(time, to, referralLevel, referralAddress, recommendedAddress);
        Pageable pageable = PageRequest.of((page - 1) * size, size);
        return new PageImpl<>(referDTOS, pageable, count);
    }

    public long findReferralCount(String time, String to,  String referralLevel, String referralAddress, String recommendedAddress){
        StringBuilder countSqlBuilder = new StringBuilder("select count(1) from (\n" +
                "select b.time as time, '1' as level, b.user as referredUser  \n" +
                "from blind_box b where recommender = :referralAddress \n" +
                "UNION\n" +
                "select x.time as time, '2' as level,  x.user as referredUser \n" +
                "from blind_box x \n" +
                "where recommender in (select user from blind_box where recommender = :referralAddress)) t \n" +
                "where 1= 1 ");
        if (StringUtils.isNotEmpty(time) && StringUtils.isNotEmpty(to)){
            countSqlBuilder.append("and from_unixtime(t.time) between from_unixtime(:startTime) and FROM_UNIXTIME(:endTime)");
        }
        if (StringUtils.isNotEmpty(referralLevel)){
            countSqlBuilder.append("and level = :referralLevel ");
        }
        if (StringUtils.isNotEmpty(recommendedAddress)){
            countSqlBuilder.append(" and referredUser = :recommendedAddress ");
        }
        Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());
        if (null != time && null != to){
            countQuery.setParameter("startTime", time);
            countQuery.setParameter("to", to);
        }
        if (null != referralLevel){
            countQuery.setParameter("referralLevel", referralLevel);
        }
        if (null != recommendedAddress){
            countQuery.setParameter("recommendedAddress", recommendedAddress);
        }
        countQuery.setParameter("referralAddress", referralAddress);
        return ((Number)countQuery.getSingleResult()).longValue();
    }

    public long getMyReferrersCount(String userAddress){
        return blindBoxRepository.countBlindBoxByUserAndRecommenderNotNull(userAddress);
    }

    @Transactional
    public void create(BlindBox box){
        blindBoxRepository.save(box);
    }
}
