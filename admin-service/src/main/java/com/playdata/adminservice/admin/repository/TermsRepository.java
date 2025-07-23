package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.admin.repository.custom.TermsRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long>, TermsRepositoryCustom {

    /**
     * 특정 ID와 카테고리에 해당하며 활성 상태가 true인 Terms 엔티티를 조회한다.
     *
     * @param id 조회할 Terms의 고유 ID
     * @param category 조회할 TermsCategory (예: TERMS, POLICY, QNA)
     * @return 조건에 맞는 Terms 엔티티를 Optional로 감싸 반환, 없으면 Optional.empty()
     */
    Optional<Terms> findByTermsIdAndCategoryAndActiveIsTrue(Long id, TermsCategory category);

    /**
     * 특정 카테고리에 해당하며 활성 상태가 true인 Terms 중
     * 가장 최신(termsId 기준 내림차순) 엔티티 하나를 조회한다.
     *
     * @param termsCategory 조회할 TermsCategory (예: TERMS, POLICY, QNA)
     * @return 조건에 맞는 최신 Terms 엔티티를 Optional로 감싸 반환, 없으면 Optional.empty()
     */
    Optional<Terms> findTopByCategoryAndActiveIsTrueOrderByTermsIdDesc(TermsCategory termsCategory);


}
