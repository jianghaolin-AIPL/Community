// 要给form定义一个事件（不是按钮，因为按钮一点就提交了），当我们点submit按钮的时候会触发表单的提交事件，我们需要对这个事件做些定义，定义这个表单具体怎么提交
$(function(){  // 页面加载完后会调这个函数
    // 页面加载完后需要给这个form定义一个事件
    $("#uploadForm").submit(upload); // upload是一个方法名，还没写，一会写。当点击表单提交按钮，触发表单提交事件时，这个事件由upload这个函数来处理
});

function upload() {
    // 要想异步的提交表单，这个表单比较特别，里面包含的是文件，所以不能写$.post，因为$.post是个简化的方式，有些参数没有办法设置
    // $.get、$.post都是对$.ajax的简化
    $.ajax({
        url: "http://upload.qiniup.com", // 请求提交给谁（七牛云），在七牛云官网，开发者工具-对象存储-存储区域
        method: "post",
        processData: false, // 不要把表单的内容转为字符串，默认的情况浏览器会将表单的内容转为字符串提交给服务器，上传文件不应该转为字符串
        contentType: false, // 按理说contentType写的是html、json等，传的数据的类型，而false是不让jQuery设置上传的类型，浏览器会自动的进行设置
        // 因为在提交文件的时候，文件和别的数据不一样，文件是二进制的，它和别的数据混编在一起的时候，浏览器会给它加一个随机的边界字符串，好拆分内容
        // 如果这里指定了contentType，那么jQuery就会自动的设置类型，边界就设置不上，就会导致传的文件会有问题
        data: new FormData($("#uploadForm")[0]),
        success: function(data) { // 七牛云返回的就是json字格式数据，不用解析
            if(data && data.code = 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName": $("input[name='key']").val()},
                    function(data) {
                        data = $.parseJSON(data); // 我们服务器返回的是普通字符串，但格式是json，需要解析成json字符串
                        if(data.code == 0) {
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            }else {
                alert("上传失败！");
            }
        }
    });
    return false;
    // 如果最后不return false，尽管这个方法能做一些逻辑处理，但最终还是会尝试提交这个表单，但是我们又没有写action，就会有问题
    // return false就可以让它不要再往下提交了，上面的逻辑已经把这个请求给处理了，return false可以让事件到此为止，不再继续向下执行默认底层的事件了
}