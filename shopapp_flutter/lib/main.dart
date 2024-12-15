import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:shopapp_flutter/src/view/screen/home_screen.dart';
import 'package:shopapp_flutter/src/view/screen/login_screen.dart';

import 'core/app_theme.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      scrollBehavior: const MaterialScrollBehavior().copyWith(
        dragDevices: {
          PointerDeviceKind.mouse,
          PointerDeviceKind.touch,
        },
      ),
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightAppTheme,
      initialRoute: '/', // Đặt route mặc định
      routes: {
        '/': (context) => const LoginPage(), // Trang login là route mặc định
        '/home': (context) => const HomeScreen(), // Định nghĩa route cho HomeScreen
      },
    );
  }
}
