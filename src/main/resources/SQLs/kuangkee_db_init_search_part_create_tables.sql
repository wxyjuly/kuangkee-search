/*建表语句*/

/*==============================================================*/
/* Table: tb_brand    品牌                                      */
/*==============================================================*/
create table tb_brand 
(
   brand_id             int                            primary key auto_increment 
		comment '品牌ID，自增',
   brand_name           varchar(32)                    null 
		comment '品牌名称',
   order_weight         varchar(2)                     null default '1' 
		comment '排序权重（1-10），数字越大，关键词放在越前面',
   creater              varchar(32)                    null comment '创建者（刚开始默认系统创建，后续通过搜索结果更新）',
   is_show              varchar(1)                     null default '1' 
		comment '是否显示【1：显示，0：不显示】；是否在搜索页面显示',
   create_time          datetime                       not null 
		comment '创建时间，当前时间;now()', 
   update_time          timestamp                      not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP 
		comment '修改时间'
);


/*==============================================================*/
/* Table: tb_user_search_log    --搜索                          */
/*==============================================================*/
create table tb_user_search_log 
(
   log_id               int                            primary key auto_increment 
		comment '日志id',
   user_id              int                            null
		comment '用户id，与User表关联',
   brand_id             int                            null
		comment '品牌编码',
   brand_name           varchar(32)                    null
		comment '品牌名称',
   original_content     varchar(256)                   null
		comment '原始用户搜索内容',		
   search_content       varchar(256)                   not null
		comment '搜索内容',
   user_name            varchar(128)                   null
		comment '用户姓名（冗余字段）',
   phone                varchar(16)                    null
		comment '用户手机号码（冗余字段）',
   status               varchar(1)                     null
		comment '状态（0：删除，1：正常｛默认值｝）',
   is_match             varchar(1)                     null default '1'
		comment '是否存在搜索结果，0（无搜索结果）；1(有搜索结果)',
   longitude            varchar(16)                    null
		comment '经度',	
   latitude             varchar(16)                    null
		comment '纬度',
   ip                   varchar(32)                    null
		comment '搜索用户ip',	
   create_time          datetime                       not null
		comment '创建时间（系统当前时间）;now()',
   update_time          timestamp                      not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
		comment '最后修改时间'
) ;



/*==============================================================*/
/* Table: tb_article       -- 文章基本信息                      */
/*==============================================================*/
create table tb_article
(
   article_id           int                            primary key auto_increment
		comment '自增编号',
   brand_id             varchar(8)                     not null
		comment '品牌id',
   brand_name           varchar(32)                    not null
		comment '品牌名称（冗余字段）',
   error_code                varchar(256)              null
		comment '挖掘机错误编码',		
   title                varchar(256)                   null
		comment '文章标题',
   sub_title            varchar(512)                   null
		comment '子标题',
   img_search_small     varchar(512)                   null
		comment '文章搜索图片缩略图URL',
   img_content_big              varchar(512)                   null
		comment '文章图片正文大图',	
   img_content_small            varchar(512)                   null
		comment '文章图片正文小图',		
   source_url           varchar(512)                   null
		comment '文章来源',
   content              text                           null
		comment '文章正文；第一版不拆分',
   is_searchable              varchar(1)               null default '1'
		comment '是否显示【1:显示；0：不显示】，若为显示，会将相关文章保存到solr；人工创建，默认显示；爬虫创建，默认不显示',
   creater              varchar(256)                   null
		comment '创建者【创建者工号或者爬虫或用户生成】',
   creater_desc         varchar(512)                   null
		comment '创建者信息说明',
   read_times           varchar(10)                    null default '0'	
		comment '文章阅读次数',
   create_time          datetime                       not null
		comment '创建时间，当前时间;now()',
   update_time          timestamp                      not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
		comment '修改时间'
) ;



/*==============================================================*/
/* Table: tb_article_desc    --文章明细信息[保存text大字段内容] */
/*==============================================================*/
create table tb_article_desc 
(
   article_id           int                            not null
		comment '外键，关联tb_article,article_id',
   content              text                           null
		comment '文章正文',
   create_time          datetime                       not null
		comment '创建时间，当前时间;now()',
   update_time          timestamp                      not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
		comment '修改时间'
) ;

