package com.playdata.adminservice.admin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = 818075848L;

    public static final QAdmin admin = new QAdmin("admin");

    public final com.playdata.adminservice.common.entity.QBaseTimeEntity _super = new com.playdata.adminservice.common.entity.QBaseTimeEntity(this);

    public final BooleanPath active = createBoolean("active");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

