create table if not exists `measure_string` (
    `database_hash` bigint not null,
    `bucket_hash` bigint not null,
    `hash` bigint not null,
    `value` varchar(512),
    `type` int not null,
    `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary KEY (`database_hash`, `bucket_hash`, `hash`, `modified_at`, `type`)
) DEFAULT CHARSET=utf8;

create table if not exists `measure_text` (
    `database_hash` bigint not null,
    `bucket_hash` bigint not null,
    `hash` bigint not null,
    `value` text,
    `type` int not null,
    `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary KEY (`database_hash`, `bucket_hash`, `hash`, `modified_at`, `type`)
) DEFAULT CHARSET=utf8;

create table if not exists `measure_decimal` (
    `database_hash` bigint not null,
    `bucket_hash` bigint not null,
    `hash` bigint not null,
    `value` double,
    `type` int not null,
    `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary KEY (`database_hash`, `bucket_hash`, `hash`, `modified_at`, `type`)
) DEFAULT CHARSET=utf8;

create table if not exists `measure_integer` (
    `database_hash` bigint not null,
    `bucket_hash` bigint not null,
    `hash` bigint not null,
    `value` bigint,
    `type` int not null,
    `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary KEY (`database_hash`, `bucket_hash`, `hash`, `modified_at`, `type`)
) DEFAULT CHARSET=utf8;

create table if not exists `login` (
    `id` bigint not null auto_increment,
    `token` varchar(255),
    `json_settings` varchar(4000),
    `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary KEY (`id`, `token`)
) DEFAULT CHARSET=utf8;