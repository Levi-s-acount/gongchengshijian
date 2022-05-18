window.addEventListener('DOMContentLoaded',function(){
    var btn=document.querySelectorAll('.btn');
    for(let i=0;i<btn.length;i++){
        btn[i].addEventListener('click',function(e){
            let xhr=new XMLHttpRequest();
            let dataObj={};
            e.preventDefault();
            let data=$('#form-login').serialize();
            data=data.split('&');
            for(let i=0;i<data.length;i++){
                let temp=data[i].split('=');
                dataObj[temp[0]]=temp[1];
            }
            console.log(dataObj);
        })
    }
    // 验证码加载
    var vef=document.querySelectorAll('.vef');
    // 页面首次加载时请求验证码
    for(let i=0;i<vef.length;i++){
        let xhr=new XMLHttpRequest();
        xhr.open('get','http://106.12.147.187:8777/user/getCode');
        xhr.timeout=2000;
        xhr.ontimeout=function(){
            alert('刷新超时');
        }
        xhr.send();
        xhr.addEventListener('readystatechange',function(){
            if(xhr.readyState==4){
                // console.log(URL.createObjectURL(xhr.response));
                // vef[i].src=URL.createObjectURL(xhr.response);
                vef[i].src=xhr.response;
                // console.log(xhr.response);       
            }
        })
        // 刷新验证码
        vef[i].addEventListener('click',function(){
            let xhr=new XMLHttpRequest();
            xhr.open('get','http://106.12.147.187:8777/user/getCode?'+Math.random());
            xhr.timeout=2000;
            xhr.ontimeout=function(){
                xhr.abort();
                alert('刷新超时');
            }
            xhr.send();
            xhr.addEventListener('readystatechange',function(){
                if(xhr.readyState==4){
                    vef[i].src=xhr.response;
                    // console.log(xhr.response);       
                }
            })
        })
    }
})