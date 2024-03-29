// (1) 회원정보 수정
function update(userid,event) {
    event.preventDefault() // 폼태그 액션을 막기
    let data = $("#profileUpdate").serialize()

    console.log(data);

    $.ajax({
        type: "put",
        url: `/api/user/${userid}`,
        data: data,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "json"
    }).done(res => { // HTTP Status 상태코드가 200번대 일 때, done이 실행된다.
        console.log("update 성공", res);
        alert("회원정보가 성공적으로 수정되었습니다.");
        location.href = `/user/${userid}`;
    }).fail(error => { // HTTP Status 상태코드가 200번대가 아닐 때, fail이 실행된다.
        if (error.data == null) { // message만 응답
            alert(error.responseJSON.message);
        } else { // errorMap이 포함된 응답
            alert("회원정보 수정에 실패하였습니다. 원인 : " +
                JSON.stringify((error.responseJSON.data)));
        }
    });
}