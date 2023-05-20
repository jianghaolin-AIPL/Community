$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");

	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 异步发送post请求
	$.post(
	    CONTEXT_PATH + "/letter/send",
	    {"toName":toName,"content":content},
	    function(data) {
	        // 这个data是普通的字符串（满足json的格式） 需要把它转换为js对象
	        data = $.parseJSON(data);
	        if(data.code == 0) {
	            $("#hintBody").text("发送成功！");
	        } else {
	            $("#hintBody").text(data.msg);
	        }

	        $("#hintModal").modal("show");
            setTimeout(function(){
            	$("#hintModal").modal("hide");
            	window.location.reload(); // 重载当前页面（刷新）
            }, 2000);
	    }
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}