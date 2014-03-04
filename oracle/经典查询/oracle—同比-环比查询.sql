-- 1. ddl语句
CREATE TABLE "ORH"."T_SALES" 
   (	"DT" DATE, 
      "CNT" NUMBER
   ); 

-- 2. insert数据语句
Insert into T_SALES (DT,CNT) values (to_date('2012-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),26);
Insert into T_SALES (DT,CNT) values (to_date('2012-02-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),40);
Insert into T_SALES (DT,CNT) values (to_date('2012-03-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),82);
Insert into T_SALES (DT,CNT) values (to_date('2013-02-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),50);
Insert into T_SALES (DT,CNT) values (to_date('2013-03-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),120);
Insert into T_SALES (DT,CNT) values (to_date('2012-04-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),90);
Insert into T_SALES (DT,CNT) values (to_date('2012-05-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),95);
Insert into T_SALES (DT,CNT) values (to_date('2012-06-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),105);
Insert into T_SALES (DT,CNT) values (to_date('2012-07-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),120);
Insert into T_SALES (DT,CNT) values (to_date('2012-08-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),132);
Insert into T_SALES (DT,CNT) values (to_date('2012-09-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),125);
Insert into T_SALES (DT,CNT) values (to_date('2012-10-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),136);
Insert into T_SALES (DT,CNT) values (to_date('2013-04-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),130);
Insert into T_SALES (DT,CNT) values (to_date('2012-12-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),180);
Insert into T_SALES (DT,CNT) values (to_date('2013-05-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),152);
Insert into T_SALES (DT,CNT) values (to_date('2013-06-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),114);
Insert into T_SALES (DT,CNT) values (to_date('2013-07-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),126);
Insert into T_SALES (DT,CNT) values (to_date('2013-08-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),128);
Insert into T_SALES (DT,CNT) values (to_date('2013-09-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),154);
Insert into T_SALES (DT,CNT) values (to_date('2013-10-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),134);
Insert into T_SALES (DT,CNT) values (to_date('2013-12-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),156);
Insert into T_SALES (DT,CNT) values (to_date('2013-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),150);
Insert into T_SALES (DT,CNT) values (to_date('2012-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),155);
Insert into T_SALES (DT,CNT) values (to_date('2013-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),46);

--########## 环比
-- 当数据中不是每个月都有数据时，可采用下面的技巧来实现
WITH t_d AS
  (SELECT '2012-'||lpad(rownum,2,'0') mm FROM dual CONNECT BY level<=12
  )　select *
FROM m; 

-- 查询sql
WITH t_d AS
  (
    SELECT '2012-'||lpad(rownum,2,'0') mm FROM dual CONNECT BY level<=12
      UNION ALL
    SELECT '2013-'||lpad(rownum,2,'0') mm FROM dual CONNECT BY level<=12
  ),
  t_s AS
  (
  SELECT TO_CHAR(dt,'yyyy-mm') mm,
    SUM(cnt) cnt
  FROM t_sales
  GROUP BY TO_CHAR(dt,'yyyy-mm')
  )
SELECT mm "月份",
  cnt "销量",
  lag1_cnt "上月销量",
  lag12_cnt "同比销量",
  cnt -lag1_cnt "环比增量",
  cnt -lag12_cnt "同比增量",
  ROUND(
  CASE
    WHEN NVL(cnt,0) = 0 OR lag1_cnt    IS NULL
      THEN NULL
    ELSE ((cnt-lag1_cnt)/cnt) * 100
  END , 1 ) "环比增加比例(%)",
  ROUND(
  CASE
    WHEN NVL(cnt,0) = 0 OR lag12_cnt   IS NULL
      THEN NULL
    ELSE ((cnt-lag12_cnt)/cnt) * 100
  END, 1) "同比增加比例(%)"
FROM
  (SELECT t_d.mm,
    t_s.cnt,
    lag(t_s.cnt,1) over(order by t_s.mm) lag1_cnt,
    lag(t_s.cnt,12) over(order by t_s.mm) lag12_cnt
  FROM t_d
  LEFT JOIN t_s
  ON t_d.mm = t_s.mm
  );
