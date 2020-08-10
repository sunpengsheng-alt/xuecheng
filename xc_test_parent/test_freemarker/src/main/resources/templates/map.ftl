${allUser.user1.username} <br/>
${allUser['user1'].username} <br/>



<hr/>
<#list allUser?keys as k>
    ${k} -- ${allUser[k].username} -- ${allUser[k].password} -- ${allUser[k].age} <br/>
</#list>

