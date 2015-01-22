
-- 创建测试表
create table T_TEST
(
  line    VARCHAR2(20),
  station VARCHAR2(2000)
);

-- 插入测试数据
insert into t_test (LINE, STATION)
values ('301', '站点1，站点2，站点3，站点4');

insert into t_test (LINE, STATION)
values ('302', '站点4，站点5，站点6，站点7');

insert into t_test (LINE, STATION)
values ('303', '站点2，站点3，站点4，站点8');

insert into t_test (LINE, STATION)
values ('304', '站点9，站点1，站点4，站点6');


-- 查询语句
select station, wmsys.wm_concat(line) line
  from (select line, REGEXP_SUBSTR(station, '[^，]+', 1, LEVEL) station
          from T_test
        CONNECT BY LEVEL <= REGEXP_COUNT(station, '[^，]+')
               and rowid = prior rowid
               and prior dbms_random.value is not null)
 group by station
