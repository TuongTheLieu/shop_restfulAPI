import 'dart:convert'; // Để xử lý JSON
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
// Thư viện SharedPreferences
import 'package:http/http.dart' as http;
import 'package:shopapp_flutter/src/view/screen/register_screen.dart';
// Gói http để gọi API


class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  // Hàm gọi API và lưu token
  Future<void> _login() async {
    // dùng 10.0.2.2 nếu dùng giả lập
    const String apiUrl = "http://localhost:8088/api/v1/users/login";

    // Dữ liệu cần gửi đến API
    final Map<String, String> data = {
      "phone_number": _emailController.text,
      "password": _passwordController.text,
    };

    try {
      // Gửi yêu cầu POST đến API
      final response = await http.post(
        Uri.parse(apiUrl),
        headers: {"Content-Type": "application/json"},
        body: jsonEncode(data),
      );

      if (response.statusCode == 200) {
        // Thành công: xử lý dữ liệu trả về
        final responseData = response.body; // Nếu body là chuỗi token trực tiếp
        if (responseData.isNotEmpty) {
          await _saveToken(responseData); // Lưu token
          print("Đăng nhập thành công, token: $responseData");
          Navigator.of(context).pushReplacementNamed('/home'); // Điều hướng
        } else {
          _showErrorDialog("Token không hợp lệ.");
        }
      } else {
        // Thất bại: hiển thị thông báo lỗi
        final errorData = jsonDecode(response.body);
        print("Đăng nhập thất bại: ${errorData['message']}");
        _showErrorDialog(errorData['message']);
      }
    } catch (e) {
      // Xử lý lỗi (ví dụ: không kết nối được server)
      print("Lỗi: $e");
      _showErrorDialog("Không thể kết nối đến server.");
    }
  }

  // Hàm lưu token vào SharedPreferences
  Future<void> _saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('auth_token', token);
  }

  // Hàm hiển thị thông báo lỗi
  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Lỗi đăng nhập"),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(),
            child: const Text("Đóng"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final Size size = MediaQuery.of(context).size;
    return Scaffold(
      body: SingleChildScrollView(
        child: Container(
          width: size.width,
          height: size.height,
          padding:
          const EdgeInsets.only(left: 20, right: 20, top: 100, bottom: 80),
          color: Colors.white,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Hello , \nWelcome Back',
                style: Theme.of(context).textTheme.headline1,
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  const SizedBox(height: 50),
                  Container(
                    padding:
                    const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                    decoration: BoxDecoration(
                      color: Theme.of(context).primaryColorLight,
                      borderRadius: const BorderRadius.all(Radius.circular(20)),
                    ),
                    child: TextField(
                      controller: _emailController,
                      decoration: const InputDecoration(
                        border: InputBorder.none,
                        hintText: 'Email or Phone Number',
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Container(
                    padding:
                    const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                    decoration: BoxDecoration(
                      color: Theme.of(context).primaryColorLight,
                      borderRadius: const BorderRadius.all(Radius.circular(20)),
                    ),
                    child: TextField(
                      controller: _passwordController,
                      obscureText: true,
                      decoration: const InputDecoration(
                        border: InputBorder.none,
                        hintText: 'Password',
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Text(
                    'Forgot Password?',
                    style: Theme.of(context).textTheme.bodyText1,
                  )
                ],
              ),
              Column(
                children: [
                  ElevatedButton(
                    onPressed: _login, // Gọi hàm đăng nhập khi nhấn nút
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      padding: const EdgeInsets.all(18),
                      elevation: 0,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                    ),
                    child: const Center(child: Text('Login')),
                  ),
                  const SizedBox(height: 30),
                  GestureDetector(
                    onTap: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (context) => const SignupPage(),
                        ),
                      );
                    },
                    child: Text(
                      'Create account',
                      style: Theme.of(context).textTheme.bodyText1,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
