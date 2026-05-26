import json
import base64
import numpy as np
import cv2
from kafka import KafkaConsumer, KafkaProducer
from ultralytics import YOLO

print("Đang tải mô hình AI...")
model = YOLO('yolov8n.pt') 

def process_frames():
    consumer = KafkaConsumer(
        'camera_frames',
        bootstrap_servers=['localhost:9092'],
        value_deserializer=lambda x: json.loads(x.decode('utf-8'))
    )
    producer = KafkaProducer(
        bootstrap_servers=['localhost:9092'],
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )
    
    print("🧠 [Server 2] AI đang chạy và đợi hình ảnh...")
    for message in consumer:
        data = message.value
        
        # Giải mã hình ảnh từ Base64
        img_bytes = base64.b64decode(data['image'])
        np_arr = np.frombuffer(img_bytes, np.uint8)
        img = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
        
        # Nhận diện người (class 0)
        results = model(img, classes=[0], verbose=False)
        person_count = len(results[0].boxes)
        
        # Gửi kết quả
        result_payload = {
            "camera_id": data['camera_id'],
            "frame_id": data['frame_id'],
            "person_count": person_count
        }
        producer.send('detection_results', value=result_payload)
        producer.flush()
        print(f"👁️ Đã xử lý Frame {data['frame_id']}: Phát hiện {person_count} người")

if __name__ == "__main__":
    process_frames()
