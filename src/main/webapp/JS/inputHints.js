window.addEventListener('DOMContentLoaded',function(){
    const ib=document.querySelectorAll('.input-backend');
    const showbox=document.querySelectorAll('.showbox');
    const xhr=new XMLHttpRequest();
    const url=['http://106.12.147.187:8777/user/getUniversity','http://106.12.147.187:8777/user/getCollege?universityId=',
    'http://106.12.147.187:8777/user/getMajor?collegeId=','http://106.12.147.187:8777/user/getGrade?majorId=',
    'http://106.12.147.187:8777/user/getClass?gradeId='];
    var temp=null;
    var tempObj={};
    for(let i=0;i<ib.length;i++){
        if(i===0){
            ib[i].addEventListener('focus',function(){
                if(this.nextElementSibling.children[0].innerHTML!=''){
                    this.nextElementSibling.children[0].innerHTML='';
                }
                request(url[i],this.getAttribute('attrId'),xhr,ib,showbox,i);
            })
            hid(ib[i]);
        }else if(i===4){
            ib[i].addEventListener('focus',function(){
                if(this.nextElementSibling.children[0].innerHTML!=''){
                    this.nextElementSibling.children[0].innerHTML='';
                }
                const mj=document.querySelector('.mj');
                let tempUrl=url[i]+showbox[i-1].children[0].parentNode.previousElementSibling.getAttribute('cid')+'&majorId='+mj.getAttribute('cid');
                request(tempUrl,this.getAttribute('attrId'),xhr,ib,showbox,i);
            })
            hid(ib[i]);
        }else{
            ib[i].addEventListener('focus',function(){
                if(this.nextElementSibling.children[0].innerHTML!=''){
                    this.nextElementSibling.children[0].innerHTML='';
                }
                let tempUrl=url[i]+showbox[i-1].children[0].parentNode.previousElementSibling.getAttribute('cid');
                // console.log(url[i]);
                request(tempUrl,this.getAttribute('attrId'),xhr,ib,showbox,i);
            })
            hid(ib[i]);
        }
    }
})