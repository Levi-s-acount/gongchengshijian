window.addEventListener('DOMContentLoaded',function(){
    const es=document.querySelector('.exam-sheet');
    const xhr=new XMLHttpRequest();
    xhr.open('get','http://106.12.147.187:8777/public/allExamInfo');
    xhr.setRequestHeader('token',sessionStorage.getItem('token'));
    // console.log(sessionStorage.getItem('token'));
    xhr.timeout=2000;
    xhr.ontimeout=function(){
        alert('网络似乎不太好');
    }
    xhr.send();
    xhr.onreadystatechange=function(){
        if(this.readyState===4){
            console.log('request sended');
            data=JSON.parse(xhr.response);
            // console.log(data['message']);
            for(let i of data['result']){
                let eb=document.createElement('div');
                eb.setAttribute('class','exam-box item-box-wrapper');
                eb.setAttribute('examId',i['examId']);
                eb.innerHTML=i['examName'];
                es.appendChild(eb);
            }    
            const ebs=document.querySelectorAll('.exam-box');
            for(let i of ebs){
                i.addEventListener('click',function(){
                    location.href='./examinfopage.html?examId='+this.getAttribute('examId');               
                })
            }
        }
    }   
})