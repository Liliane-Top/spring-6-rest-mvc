drop table if exists beer_order;

drop table if exists beer_order_line;

create table beer_order
(
    id           varchar(36) not null,
    created_date datetime(6),
    customer_ref varchar(255),
    update_date  datetime(6),
    version      integer,
    customer_id  varchar(36),
    primary key (id)
) engine = InnoDB;



create table beer_order_line
(
    id                 varchar(36) not null,
    beer_id            varchar(36) not null,
    beer_order_id      varchar(36) not null,
    created_date       datetime(6),
    update_date        datetime(6),
    order_quantity     integer,
    quantity_allocated integer,
    version            integer,
    primary key (id)
) engine = InnoDB;

ALTER TABLE beer_order
    ADD CONSTRAINT beer_order_customer_id_FK
        FOREIGN KEY (customer_id) REFERENCES customer(id);

ALTER TABLE beer_order_line
    ADD CONSTRAINT beer_orderline_beer_id_FK
        FOREIGN KEY (beer_id) REFERENCES beer(id);

ALTER TABLE beer_order_line
    ADD CONSTRAINT beer_orderline_beer_order_id_FK
        FOREIGN KEY (beer_order_id) REFERENCES beer_order(id);
