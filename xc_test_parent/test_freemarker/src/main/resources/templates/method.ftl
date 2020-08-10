<#--
    <hr/>
// 某个集合的大小
${集合名?size}
// 年月日
${日期?data}
// 时分秒
${日期?time}
// 日期 + 时间
${日期?datatime}  <br/>
// 自定义格式化
${日期?string("yyyy年MM月")}
// 不想显示为没三围分隔的数字
${数字变量?c}
// 将json字符串转换成对象
text?eval
-->

${list?size}<br/>

年月日 : ${birthday?date} <br/>
时分秒 ; ${birthday?time} <br/>
日期+时间 : ${birthday?datetime} <br/>
自定义格式化 : ${birthday?string("yyyy年MM月")} <br/>

${money} <br/>
${money?c} <br/>
${text} <br/>
<#assign user=text?eval/>
${user.username}