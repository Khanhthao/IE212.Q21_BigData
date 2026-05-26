import cv2
import json
import base64
import time
from kafka import KafkaProducer

def stream_camera():
    producer = KafkaProducer(
        bootstrap_servers=['localhost:9092'],
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )
    
    # Số 0 đại diện cho webcam mặc định của laptop
    cap = cv2.VideoCapture(0) 
    print("📷 [Server 1] Đang mở Camera và gửi luồng dữ liệu...")
    
    frame_id = 0
    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break
            
        _, buffer = cv2.imencode('.jpg', frame)
        frame_base64 = base64.b64encode(buffer).decode('utf-8')
        
        payload = {
            "camera_id": "cam_01",
            "frame_id": frame_id,
            "image": frame_base64
        }
        
        producer.send('camera_frames', value=payload)
        print(f"📤 Đã gửi Frame {frame_id}")
        frame_id += 1
        
        # Dừng 0.5s mỗi frame để máy không bị quá tải khi test
        time.sleep(0.5) 

    cap.release()

if __name__ == "__main__":
    stream_camera()