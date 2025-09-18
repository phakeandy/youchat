<template>
  <footer
    class="footer sm:footer-horizontal footer-center bg-base-300 text-base-content p-4"
    data-testid="footer"
  >
    <aside data-testid="footer-content">
      <p data-testid="copyright">Copyright © {{ new Date().getFullYear() }} - 基于 Vue 3 + DaisyUI + Tailwind CSS 构建</p>
    </aside>
  </footer>
</template>
