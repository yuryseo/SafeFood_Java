# 8월 관통 프로젝트 제출

<구현 기능>

[FoodDaoImpl 클래스]

    loadData() : FoodNutritionSaxPaser를 이용하여 Food 데이터들을 읽어와 DB에 저장
    foodCount(foodpageBean bean) : 검색 조건(key)와 검색 단어(word) 정보를 담은 FoodPageBean을 인자로 받아 검색 조건(name, maker, material )에 맞는 데이터 개수를 구하여 리턴
    searchAll(FoodPageBean bean) : 검색 조건(key)와 검색 단어(word) 정보를 담은 FoodPageBean을 인자로 받아	 검색 조건(name, maker, material )에 맞는 식품 정보(Food)를 DB에서 검색해서 반환
    search(int code) : 식품 코드에 해당하는 식품 정보를 조회
    
[FoodServiceImpl 클래스]

    search(int code) : code와 같은 식품 정보를 FoodDaoImpl 클래스를 이용하여 검색 하고, 원재료에 알레르기 성분이 있는지 확인하여 Food객체에 알레르기 정보를 추가하여 리턴