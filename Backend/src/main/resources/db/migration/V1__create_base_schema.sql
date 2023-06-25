create table client
(
    id             uuid not null
        primary key,
    email          varchar(255),
    fullname       varchar(255),
    identification varchar(255),
    telephone      varchar(255),
    type           varchar(255)
);



create table payment
(
    id      uuid not null
        primary key,
    payment varchar(255)
);



create table invoice
(
    id                    uuid not null
        primary key,
    description           varchar(255),
    invoice_number        varchar(255),
    issue_date            date,
    iva                   double precision,
    status                smallint,
    subtotal_excludingiva numeric(38, 2),
    total                 numeric(38, 2),
    client_id             uuid
        constraint fk6y01j0975eqwmnb0gckttrbj2
            references client,
    payment_id            uuid
        constraint fkbaxa82hce5x7dqj0sotnc1cxf
            references payment,
    user_id               uuid
        constraint fkjunvl5maki3unqdvljk31kns3
            references auth_schema."user"
);



create table credit_note
(
    id                 uuid not null
        primary key,
    credit_note_number varchar(255),
    issue_date         date,
    invoice_id         uuid
        constraint fk89mv8mrynbjxoc6khjkbfibeb
            references invoice
);



create table credit_note_detail
(
    id             uuid not null
        primary key,
    discount       double precision,
    price          numeric(38, 2),
    quantity       integer,
    subtotal       double precision,
    credit_note_id uuid
        constraint fk3ffo8lfgaicb2163yo7mmad5b
            references credit_note
);


create table promotion
(
    id          uuid not null
        primary key,
    description varchar(255),
    status      boolean,
    value       double precision
);


create table supplier
(
    id             uuid not null
        primary key,
    email          varchar(255),
    identification varchar(255),
    name           varchar(255),
    telephone      varchar(255),
    address        varchar(255),
    status      boolean
);



create table purchase
(
    id                    uuid not null
        primary key,
    iva                   double precision,
    purchase_date         date,
    purchase_number       varchar(255),
    subtotal_excludingiva numeric(38, 2),
    total                 numeric(38, 2),
    payment_id            uuid
        constraint fkfx22le0qre4gvfot8v1y13bjw
            references payment,
    supplier_id           uuid
        constraint fk8omm6fki86s9oqk0o9s6w43h5
            references supplier,
    user_id               uuid
        constraint fk86i0stm7cqsglqptdvjij1k3m
            references auth_schema."user"
);



create table tax
(
    id         uuid not null
        primary key,
    percentage double precision,
    status     boolean,
    tax        varchar(255)
);

create table category
(
    id           uuid not null
        primary key,
    category     varchar(255),
    status       boolean,
    promotion_id uuid
        constraint fkdq84696i7fqlho1jb3nyyw7cl
            references promotion,
    tax_id       uuid
        constraint fk4dshg6l621xcugs67rshsdwxm
            references tax
);



create table product
(
    id           uuid not null
        primary key,
    code         varchar(255),
    max_stock    integer,
    min_stock    integer,
    name         varchar(255),
    price        double precision,
    quantity     double precision,
    status       boolean,
    category_id  uuid
        constraint fk1mtsbur82frn64de7balymq9s
            references category,
    promotion_id uuid
        constraint fkcli9x921yidy04cx25k6m46fy
            references promotion,
    tax_id       uuid
        constraint fk9lk9bo7vg7ug89vmn99wj40eh
            references tax
);



create table invoice_detail
(
    id         uuid not null
        primary key,
    discount   double precision,
    price      double precision,
    quantity   integer,
    subtotal   numeric(38, 2),
    invoice_id uuid
        constraint fkit1rbx4thcr6gx6bm3gxub3y4
            references invoice,
    product_id uuid
        constraint fkbe6c21nke5fy4m3vw00f23qsf
            references product
);



create table purchase_detail
(
    id          uuid not null
        primary key,
    code        varchar(255),
    discount    double precision,
    quantity    integer,
    subtotal    numeric(38, 2),
    product_id  uuid
        constraint fk79a6tsn4e9qfillme2u9kr3i2
            references product,
    purchase_id uuid
        constraint fk65hoe4yy1817l2vm74msb8eq5
            references purchase
);


