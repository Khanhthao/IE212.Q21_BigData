import json
from kafka import KafkaConsumer
from pymongo import MongoClient

def store_results():
    # Kết nối MongoDB (đang chạy qua Docker)
    client = MongoClient('mongodb://localhost:27017/')
    db = client['surveillance_db']
    collection = db['detections']

    # Lắng nghe dữ liệu từ Kafka
    consumer = KafkaConsumer(
        'detection_results',
        bootstrap_servers=['localhost:9092'],
        value_deserializer=lambda x: json.loads(x.decode('utf-8'))
    )
    
    print("🚀 [Server 3] Storage đang chạy và đợi kết quả...")
    for message in consumer:
        result_data = message.value
        collection.insert_one(result_data)
        print(f"✅ Đã lưu DB: Camera {result_data['camera_id']} - Frame {result_data['frame_id']} - Số người: {result_data['person_count']}")

if __name__ == "__main__":
    store_results()
