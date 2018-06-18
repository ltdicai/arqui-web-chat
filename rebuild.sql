DROP SCHEMA IF EXISTS gwtdbschema CASCADE;

CREATE SCHEMA gwtdbschema;

create table gwtdbschema.users
(
  userid   varchar(50) not null
    constraint users_pkey
    primary key,
  password text        not null
);

create table gwtdbschema.conversations
(
  conversationid varchar(50) not null
    constraint conversations_pkey
    primary key,
  externalid varchar(50) not null
);

create table gwtdbschema.messages
(
  messageid      serial      not null
    constraint messages_pkey
    primary key,
  userid         varchar(50) not null
    constraint messages_userid_fkey
    references gwtdbschema.users,
  conversationid varchar(50) not null
    constraint messages_conversationid_fkey
    references gwtdbschema.conversations
);

create table gwtdbschema.textmessages
(
  messageid integer not null
    constraint textmessages_pkey
    primary key
    constraint textmessages_messageid_fkey
    references gwtdbschema.messages,
  message   text    not null
);

create table gwtdbschema.audiomessages
(
  messageid integer not null
    constraint audiomessage_pkey
    primary key
    constraint messageid___fk
    references gwtdbschema.messages,
  message   text    not null
);

create table gwtdbschema.imagemessages
(
  messageid integer not null
    constraint imagemessages_messages_messageid_fk
    references gwtdbschema.messages,
  message   text    not null
);

create table gwtdbschema.privateconversations
(
  conversationid varchar(50) not null
    constraint privateconversation_pkey
    primary key
    constraint privateconversation_conversations_conversationid_fk
    references gwtdbschema.conversations,
  hostuserid     varchar(50) not null,
  inviteuserid   varchar(50)
);

create table gwtdbschema.groupconversations
(
  conversationid varchar(50) not null
    constraint groupconversation_conversationid_pk
    primary key
    constraint groupconversation_conversations__fk
    references gwtdbschema.conversations,
  groupname      varchar(100) not null
);

create table gwtdbschema.groups
(
  conversationid varchar(50) not null
    constraint groups_pkey
    primary key
    constraint groups_conversations__fk
    references gwtdbschema.conversations,
  userid         varchar(50) not null
    constraint groups_users__fk
    references gwtdbschema.users
);
