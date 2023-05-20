// 通常在页面加载完以后（html标签都加载完了）这时用js标签动态绑定一个事件
// 这句话的意思和js里的window.unload=function是一样的，等于页面加载事件的意思，这个函数是在页面加载完以后会调用的
$(function(){
    $("#topBtn").click(setTop); // .click():绑定一个单击事件 单击事件绑定一个函数(setTop)，这个函数一会要写
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);
})

function like(btn, entityType, entityId, entityUserId, postId) { // 第一个参数是那个超链接，但我们是当按钮来用的
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?"已赞":"赞");
            } else {
                alert(data.msg); // 失败是通过控制器通知统一处理
            }
        }
    )
}

// 置顶
function setTop() {
    // 直接发送个异步的请求传参就行了
    $.post(
        CONTEXT_PATH + "/discuss/top",
        {"id":$("#postId").val()},
        function(data){
            data = $.parseJSON(data);
            if(data.code == 0) {
                // 置顶成功之后需要将置顶按钮设置为不可用
                $("#topBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    )
}

// 加精
function setWonderful() {
    // 直接发送个异步的请求传参就行了
    $.post(
        CONTEXT_PATH + "/discuss/wonderful",
        {"id":$("#postId").val()},
        function(data){
            data = $.parseJSON(data);
            if(data.code == 0) {
                // 置顶成功之后需要将置顶按钮设置为不可用
                $("#wonderfulBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    )
}

// 删除
function setDelete() {
    // 直接发送个异步的请求传参就行了
    $.post(
        CONTEXT_PATH + "/discuss/delete",
        {"id":$("#postId").val()},
        function(data){
            data = $.parseJSON(data);
            if(data.code == 0) {
                // 帖子删除以后，设置跳转到首页
                location.href = CONTEXT_PATH + "/index";
            } else {
                alert(data.msg);
            }
        }
    )
}