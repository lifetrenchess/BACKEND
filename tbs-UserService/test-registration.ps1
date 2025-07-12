# Test Registration Endpoint
Write-Host "Testing User Registration..." -ForegroundColor Green

$registrationData = @{
    userName = "Test User"
    userEmail = "test@example.com"
    userPassword = "TestPass123!"
    userRole = "USER"
    userContactNumber = "1234567890"
} | ConvertTo-Json

Write-Host "Registration Data:" -ForegroundColor Yellow
Write-Host $registrationData -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "http://localhost:9999/user-api/users" -Method POST -Body $registrationData -ContentType "application/json"
    Write-Host "✅ Registration Successful!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "❌ Registration Failed!" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
    }
}

Write-Host "`nTesting Login..." -ForegroundColor Green

$loginData = @{
    userEmail = "test@example.com"
    userPassword = "TestPass123!"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:9999/user-api/users/login" -Method POST -Body $loginData -ContentType "application/json"
    Write-Host "✅ Login Successful!" -ForegroundColor Green
    Write-Host "Token: $loginResponse" -ForegroundColor Green
} catch {
    Write-Host "❌ Login Failed!" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
} 