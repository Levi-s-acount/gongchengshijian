window.addEventListener('DOMContentLoaded',function(){
    // 页面交互效果
    var currentCont='login';
    var navUl=document.querySelector('.nav-ul');
    navUl.addEventListener('click',function(e){
        if(e.target.firstElementChild===null){
            for(var i=0; i<navUl.children.length;i++){
                navUl.children[i].firstElementChild.style='color: rgba(0, 0, 0, 0.77);';
            }
            e.target.style='border-bottom: 3px solid #008ABC; color:rgba(0, 0, 0, 1);';
            // console.log('formarea-'+e.target.className);
            var fa=document.querySelector('.formarea-'+currentCont);
            fa.style='display: none';
            fa=document.querySelector('.formarea-'+e.target.className);
            fa.style='display: flex';
            currentCont=e.target.className;
        }   
    })
    // 登录注册提交
    var btn=document.querySelectorAll('.btn');
    for(let i=0;i<btn.length;i++){
        btn[i].addEventListener('click',function(e){
            e.preventDefault();
            if(this.value==='登录'){
                let data=$('#form-login').serialize();
                data=objTrans(data);
                post('http://106.12.147.187:8777/user/login',uuid[0],data);
            }else{
                let data=$('#form-register').serialize();
                data=objTrans(data);
                console.log(data);
                post('http://106.12.147.187:8777/user/login',uuid[1],data);
            }
        })
    }
    // 验证码展示
    var vef=document.querySelectorAll('.vef');
    var uuid=[null,null];
    // 页面刷新时请求验证码
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
                uuid[i]=xhr.getResponseHeader('uuid');  
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
                    uuid[i]=xhr.getResponseHeader('uuid');   
                }
            })
        })
    }
})
