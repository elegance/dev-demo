/**
 * Q1：Google笔试的一道选择题，上海一套房子200W，每年涨价幅度10%，某程序员年薪40W，请问，该程序员不吃不喝多少年后可以有自己的房子？
 *     选项：A：5年B：7年 C：8年D：9年E：到死都没房。
 */
function test_q_1(){
        var housePrice = 200,   //当前房价 单位:w
                raises = 0.1,   //每年涨幅
                annualSlary = 40,       //年薪 40 单位:W
                saving = 0,     //存款
                years = 0,      //第几年
                infoHtml = [];  //输出信息
        while( saving < housePrice &&   //存款不够买房
                        housePrice*raises < annualSlary){       //房价的涨幅超过了年薪
                years++;        //一年过去了
                housePrice += housePrice*raises;        //房价涨了
                saving += annualSlary;  // 存款多了
                infoHtml.push(['第', years, '年',
                                        '   房价:',housePrice.toFixed(2),
                                        '   存款',saving,
                                        '   来年房价增长：',(housePrice*raises).toFixed(2)].join(''));
        }
        if(saving < housePrice){
                infoHtml.push('结论:到死都买不起！');
        }else{
                infoHtml.push(['结论：',years,'年。'].join(''));
        }
        print(infoHtml.join('\n'));
}

test_q_1();
print_sepa();

function print_sepa(){
        print('\n');
        print('=====================测试分割线=======================');
        print('\n');

