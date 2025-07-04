package com.playdata.mainservice.main.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLike is a Querydsl query type for Like
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLike extends EntityPathBase<Like> {

    private static final long serialVersionUID = 496068450L;

    public static final QLike like = new QLike("like1");

    public final com.playdata.mainservice.common.entity.QBaseTimeEntity _super = new com.playdata.mainservice.common.entity.QBaseTimeEntity(this);

    public final BooleanPath active = createBoolean("active");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    public final EnumPath<ContentType> contentType = createEnum("contentType", ContentType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> likeId = createNumber("likeId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QLike(String variable) {
        super(Like.class, forVariable(variable));
    }

    public QLike(Path<? extends Like> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLike(PathMetadata metadata) {
        super(Like.class, metadata);
    }

}

