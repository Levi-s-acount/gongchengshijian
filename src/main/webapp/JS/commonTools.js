const qb_templtae=
`<div class="ques-box">
    <div class="ques-info-box">
        <div class="order">
            
        </div>
        <div class="qinfo">
            
        </div>
    </div>
    <div class="ques">
        <div class="ques-desc">
            <p></p>
        </div>
        <div class="options-box">
            
        </div>
    </div>
</div>`
const eb_template=
`<div class="exam-box item-box-wrapper">
                    
</div>`
const btn_template=
`<div class="pre-next">
    <div class="next-pre-box">
        <input type="button" value="上一题" class="btn-pre">
        <input type="button" value="下一题" class="btn-next">
    </div>
</div>`

// 将表单提交的参数转为字符串json
function objTrans(data){
    let dataObj={};
    data=data.split('&');
    for(let i=0;i<data.length;i++){
        let temp=data[i].split('=');
        dataObj[temp[0]]=temp[1];
    }
    data=JSON.stringify(dataObj);
    return data;
}
// 隐藏后台检索结果
function hid(ib){
    ib.nextElementSibling.children[0].onmouseleave=function(){
        this.parentNode.style='display:none;';
    }
}
// 后台检索结果展示
function show(attribute,temp,ib,showbox,index){
    for(let i=0;i<temp['result'].length;i++){
        let li=document.createElement("li");
        li.innerHTML=temp['result'][i]['name'];
        li.setAttribute('q-id',temp['result'][i][attribute])
        ib[index].nextElementSibling.children[0].appendChild(li);
    }
    if(temp['result'].length!=0)ib[index].nextElementSibling.style='display : block';
    for(let i=0;i<showbox.length;i++){
        showbox[i].children[0].addEventListener('click',function(e){
            this.parentNode.previousElementSibling.value=e.target.innerHTML;
            this.parentNode.previousElementSibling.setAttribute('cid',e.target.getAttribute('q-id'));
        })
    }

}
// 请求后台检索结果
function request(url,attr,xhr,ib,showbox,index){
    xhr.open("get",url);
    xhr.timeout=2000;
    xhr.ontimeout=function(){
        console.log("后台检索超时");
    }
    xhr.send();
    xhr.onreadystatechange=function(){
        if(xhr.readyState===4){
            temp=JSON.parse(xhr.response);
            console.log(temp['result']);
            show(attr,temp,ib,showbox,index);
        }    
    }
}
// 登录请求
function post(url,uuid,requestBody){
    let xhr=new XMLHttpRequest();
    xhr.open('post',url)
    xhr.timeout=2000;
    xhr.ontimeout=function(){
        alert("登录请求超时");
    }
    xhr.setRequestHeader('uuid',uuid);
    xhr.setRequestHeader('Content-Type','application/json');
    xhr.send(requestBody);
    xhr.onreadystatechange=function(){
        if(xhr.readyState===4){
            let temp=JSON.parse(xhr.response);
            
            if(!temp['succeed']){
                alert(temp['message']);
            }else{
                sessionStorage.setItem('account',JSON.parse(requestBody)['account']);
                sessionStorage.setItem('password',JSON.parse(requestBody)['password']);
                sessionStorage.setItem('token',temp['result']['token']);
                // console.log(temp['result']['token']);
                location.href='./index.html';
            }
        }
    }
}