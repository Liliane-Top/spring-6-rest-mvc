
    alter table beer_category 
       drop 
       foreign key FK7o42knkmhb44bhnsb804o16ch;

    alter table beer_category 
       drop 
       foreign key FKrmk65j5tao1q8mp3v4mkpesie;

    alter table beer_order 
       drop 
       foreign key FK5siih2e7vpx70nx4wexpxpji;

    alter table beer_order_line 
       drop 
       foreign key FKslskqsf79v6iekvb6d3gcc1l4;

    alter table beer_order_line 
       drop 
       foreign key FKhkgofxhwx8yw9m3vat8mgtnxs;

    alter table beer_order_shipment 
       drop 
       foreign key FKqry3snbsxuavowegq87xn9gd6;

    drop table if exists beer;

    drop table if exists beer_category;

    drop table if exists beer_order;

    drop table if exists beer_order_line;

    drop table if exists beer_order_shipment;

    drop table if exists category;

    drop table if exists customer;

    create table beer (
        beer_style tinyint not null check (beer_style between 0 and 9),
        price decimal(38,2) not null,
        quantity_on_hand integer,
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        id varchar(36) not null,
        beer_name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table beer_category (
        beer_id varchar(36) not null,
        category_id varchar(36) not null,
        primary key (beer_id, category_id)
    ) engine=InnoDB;

    create table beer_order (
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        customer_id varchar(36),
        id varchar(36) not null,
        customer_ref varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table beer_order_line (
        order_quantity integer,
        quantity_allocated integer,
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        beer_id varchar(36),
        beer_order_id varchar(36),
        id varchar(36) not null,
        primary key (id)
    ) engine=InnoDB;

    create table beer_order_shipment (
        created_date datetime(6),
        last_modified_date datetime(6),
        version bigint,
        beer_order_id varchar(36),
        id varchar(36) not null,
        tracking_number varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table category (
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        id varchar(36) not null,
        description varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table customer (
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        id varchar(36) not null,
        customer_name varchar(255),
        email varchar(255),
        primary key (id)
    ) engine=InnoDB;

    alter table beer_order_shipment 
       add constraint UK_frcrfy5pxopw5hfk1ueg0bxc2 unique (beer_order_id);

    alter table beer_category 
       add constraint FK7o42knkmhb44bhnsb804o16ch 
       foreign key (beer_id) 
       references beer (id);

    alter table beer_category 
       add constraint FKrmk65j5tao1q8mp3v4mkpesie 
       foreign key (category_id) 
       references category (id);

    alter table beer_order 
       add constraint FK5siih2e7vpx70nx4wexpxpji 
       foreign key (customer_id) 
       references customer (id);

    alter table beer_order_line 
       add constraint FKslskqsf79v6iekvb6d3gcc1l4 
       foreign key (beer_id) 
       references beer (id);

    alter table beer_order_line 
       add constraint FKhkgofxhwx8yw9m3vat8mgtnxs 
       foreign key (beer_order_id) 
       references beer_order (id);

    alter table beer_order_shipment 
       add constraint FKqry3snbsxuavowegq87xn9gd6 
       foreign key (beer_order_id) 
       references beer_order (id);
