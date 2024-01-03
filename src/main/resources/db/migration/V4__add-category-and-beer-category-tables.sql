drop table if exists category;

drop table if exists beer_category;

create table category
(
    id           varchar(36) not null,
    created_date datetime(6),
    update_date  datetime(6),
    version      integer,
    description  varchar(50),
    primary key (id)
) engine = InnoDB;

create table beer_category
(
    beer_id     varchar(36) not null,
    category_id varchar(36) not null,
    primary key (beer_id, category_id),
    constraint beer_id_fk FOREIGN KEY (beer_id) references beer (id),
    constraint category_id_fk FOREIGN KEY (category_id) references category (id)
) engine = InnoDB;

