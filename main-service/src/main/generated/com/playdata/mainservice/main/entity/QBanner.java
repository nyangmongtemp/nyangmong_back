package com.playdata.mainservice.main.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = -313167817L;

    public static final QBanner banner = new QBanner("banner");

    public final com.playdata.mainservice.common.entity.QBaseTimeEntity _super = new com.playdata.mainservice.common.entity.QBaseTimeEntity(this);

    public final BooleanPath active = createBoolean("active");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Long> bannerId = createNumber("bannerId", Long.class);

    public final BooleanPath basic = createBoolean("basic");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final StringPath thumbnailImage = createString("thumbnailImage");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}

