package com.playdata.adminservice.admin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdvertisement is a Querydsl query type for Advertisement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdvertisement extends EntityPathBase<Advertisement> {

    private static final long serialVersionUID = 743317118L;

    public static final QAdvertisement advertisement = new QAdvertisement("advertisement");

    public final BooleanPath active = createBoolean("active");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath thumbnailImage = createString("thumbnailImage");

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAdvertisement(String variable) {
        super(Advertisement.class, forVariable(variable));
    }

    public QAdvertisement(Path<? extends Advertisement> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdvertisement(PathMetadata metadata) {
        super(Advertisement.class, metadata);
    }

}

