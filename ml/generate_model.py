import numpy as np
from sklearn.ensemble import RandomForestRegressor
from skl2onnx import to_onnx
import onnxruntime as rt
import os

# 1. Define the features we want to track
# Feature 0: Years of Experience (0-40)
# Feature 1: Number of Technical Skills found (0-50)
# Feature 2: Education Level (1: Boot camp, 2: Bachelor's, 3: Master's, 4: PhD)

# 2. Synthetic Training Data (Teaching the model what "Good" looks like)
X = np.array([
    [1, 2, 1],   # Entry level, few skills -> Low score
    [3, 5, 2],   # Mid level, decent skills -> Mid score
    [5, 12, 3],  # Senior level, high skills, Masters -> High score
    [10, 20, 4], # Expert level, PhD -> Top score
    [4, 2, 2],   # 4 years but low skill count -> Mid-Low score
    [0, 8, 2],   # No experience but high skill count (New Grad) -> Mid score
], dtype=np.float32)

# Match Scores (0.0 to 1.0)
y = np.array([0.15, 0.45, 0.85, 0.98, 0.35, 0.50], dtype=np.float32)

print("🚀 Training the Ranking Model...")
model = RandomForestRegressor(n_estimators=50, max_depth=5, random_state=42)
model.fit(X, y)

# 3. Define the output path (Relative to the ml/ folder)
# This path matches what we set in the build.gradle.kts
output_path = "../src/main/resources/models/resume_ranker.onnx"

# Ensure the directory exists
os.makedirs(os.path.dirname(output_path), exist_ok=True)

# 4. Convert to ONNX format
# We tell ONNX to expect an input named "X" which is a float tensor
print(f"📦 Exporting model to ONNX with opset 19...")
model_onnx = to_onnx(model, X[:1], target_opset=19)

with open(output_path, "wb") as f:
    f.write(model_onnx.SerializeToString())

print(f"✅ Success! Model saved to: {output_path}")

# 5. Quick Verification
sess = rt.InferenceSession(output_path)
test_input = np.array([[7, 15, 3]], dtype=np.float32)
prediction = sess.run(None, {"X": test_input})[0][0]
prediction_val = float(np.squeeze(prediction))
print(f"🔬 Verification Score for Senior Engineer: {prediction_val * 100:.2f}%")
