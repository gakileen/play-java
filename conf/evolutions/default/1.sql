# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table group_member (
  id                            varchar(255) not null,
  uid                           varchar(255),
  gid                           bigint,
  constraint pk_group_member primary key (id)
);


# --- !Downs

drop table if exists group_member;

