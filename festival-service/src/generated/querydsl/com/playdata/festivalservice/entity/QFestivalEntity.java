package com.playdata.festivalservice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFestivalEntity is a Querydsl query type for FestivalEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFestivalEntity extends EntityPathBase<FestivalEntity> {

    private static final long serialVersionUID = 960925458L;

    public static final QFestivalEntity festivalEntity = new QFestivalEntity("festivalEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final StringPath festivalDate = createString("festivalDate");

    public final NumberPath<Long> festivalId = createNumber("festivalId", Long.class);

    public final StringPath festivalTime = createString("festivalTime");

    public final StringPath hash = createString("hash");

    public final StringPath imagePath = createString("imagePath");

    public final StringPath location = createString("location");

    public final StringPath money = createString("money");

    public final StringPath reservationDate = createString("reservationDate");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath url = createString("url");

    public QFestivalEntity(String variable) {
        super(FestivalEntity.class, forVariable(variable));
    }

    public QFestivalEntity(Path<? extends FestivalEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFestivalEntity(PathMetadata metadata) {
        super(FestivalEntity.class, metadata);
    }

}

