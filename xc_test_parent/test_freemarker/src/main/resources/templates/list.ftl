
<table cellpadding="1">
    <tr>
        <tb>序号</tb>
        <tb>用户名</tb>
        <tb>密码</tb>
        <tb>年龄</tb>
    </tr>
    <#list users as user>
        <tr>
            <tb>${user_index + 1}</tb>
            <tb>${user.username}</tb>
            <tb>${user.password}</tb>
            <tb>${user.age}</tb>
        </tr>
    </#list>
</table>


