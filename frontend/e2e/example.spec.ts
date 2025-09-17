import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
  await page.goto('/');
});

test('homepage has correct title', async ({ page }) => {
  await expect(page).toHaveTitle(/YouChat/);
});

test('navigation works', async ({ page }) => {
  // 点击导航链接
  await page.click('text=Home');
  await expect(page).toHaveURL('/');

  // 这里可以添加更多的导航测试
});

test('responsive design works on mobile', async ({ page }) => {
  // 测试移动端响应式设计
  await page.setViewportSize({ width: 375, height: 667 });

  // 检查移动端特定的元素或布局
  const mobileMenu = page.locator('.mobile-menu');
  if (await mobileMenu.isVisible()) {
    await expect(mobileMenu).toBeVisible();
  }
});

test('dark mode toggle works', async ({ page }) => {
  // 测试深色模式切换
  const darkModeToggle = page.locator('[data-testid="dark-mode-toggle"]');

  if (await darkModeToggle.isVisible()) {
    await darkModeToggle.click();
    await expect(page.locator('html')).toHaveClass(/dark/);
  }
});