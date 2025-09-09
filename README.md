### 목적

- API를 통해 AI 챗봇을 활용할 수 있음을 시연하고, 이후 확장이 가능한 구조를 제시합니다.

### QA

1. 과제를 어떻게 분석 하셨나요?

   - 제가 느낀 과제는 chatGPT와 같은 형태의 서비스를 만들기 위한 MVP 입니다. “3시간 내 시연”을 1순위로 두고, 최소 기능과 확장성을 동시에 만족하도록 설계했습니다. 인증, 스레드와 채팅 저장, OpenAI 연동, 30분 룰을 적용해 시연 가치를 높이는 핵심 기능만 우선 구현하고, 나머지는 폴더 구조를 나눠 확장 여지를 남겼습니다. DB 스키마는 JPA로 단순화하고, 쿼리 성능은 fetch join로 최적화했습니다.

2. 과제의 진행함에 있어 AI 를 어떻게 활용 하셨나요? 어떤 어려움이 있었나요?

   - 기존 자바를 사용해왔던 저에게 코틀린은 생소한 언어이기에 AI의 도움을 받아 코드를 작성하였습니다. 하지만 AI는 서비스 전반적인 흐름을 읽지 못하여 전체적인 서비스를 이해하지 못하였고, 개별 함수를 어떤 기조로 작성해야 하는지, 어떤 구조로 API를 구현할지에 대해 설명해주어야 했습니다. 

3. 지원자가 구현하기 가장 어려웠던 1개 이상의 기능을 설명 해주세요.

   - 첫 질문 또는 마지막 사용한 스레드의 마지막 채팅 시간이 30분 이상 지났다면 새로운 스레드를 생성하는 부분에서 어려움을 겪었습니다. N:1 관계인 chat과 thread를 한 번에 가져오는 것이 효율적이었고, 이를 fetch join을 사용하였으며, 마지막 1건을 가져오기 위해 페이징 처리하였고, 이 때 join된 데이터를 모두 가져와 메모리 아웃이 발생하지 않도록 batch size를 지정하였습니다. 

4. 공개되지 않은 기준이 있지만 구현의 양은 배점이 낮습니다.
   - 대외비 문서를 학습 시킬 것이라는 점에서 RAG 추가, 유명 provider는 알고 있다는 점에서 여러 모델을 사용할 예정이라는 것을 추측할 수 있었습니다. 시간이 충분해 이 기능들도 넣는다면 더욱 좋은 서비스가 될 것입니다. 

### 1) 시스템 개요

- Kotlin1.9.x + Spring Boot 3.x.x, PostgreSQL 16.x
- 폴더 구조

```
src/main/kotlin/com/example/demo
├─ config/         # JWT, Security
├─ controller/     # REST API
├─ dto/            # 요청/응답 DTO
├─ entity/         # Person, Thread, Chat
├─ repository/     # Spring Data JPA
└─ service/        # SignUp, ChatBot, Thread
```

---

### 2) 데이터

- Person: email(unique), password(BCrypt), name, role, createdAt
- Thread: user N:1, createdAt/updatedAt
- Chat: thread 1:N, question/answer/createdAt

---

### 3) API 사양

- POST `/api/signup` → 회원 생성
- POST `/api/signup/login` → JWT 발급(`sub=userId`)
- POST `/api/chatbot/gpt` (Auth: Bearer JWT)
  - 내부 동작: JWT 활용해 userId 획득 → 기존 스레드 획득/생성 → 과거 채팅 히스토리+질문으로 OpenAI 호출 → 답변 저장 → 응답 반환

---

### 4) 시연 플로우

1. 회원가입 
2) 로그인(JWT) 
3) 질문(스레드 생성) 
4) 재질문(30분 이내: 같은 스레드) 
5) 30분 경과: 새 스레드 자동 생성

---

### 지원자 정보

- 지원자: 정수환
- 이메일: suhwani.dev@gmail.com
